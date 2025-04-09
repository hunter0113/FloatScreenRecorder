package com.evan.floatscreenrecorder.common.util;

import static com.evan.floatscreenrecorder.common.util.StrUtil.EMPTY;
import static com.evan.floatscreenrecorder.record.service.RecordingService.errorCallbackHandle;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.SchemeTypeBox;
import com.coremedia.iso.boxes.TrackBox;
import com.evan.floatscreenrecorder.common.constant.RecordErrorType;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.CencMp4TrackImplImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Mp4TrackImpl;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.util.Path;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Evan on 2021/7/7.
 * <p>
 * Description：
 */
public class VideoClipUtils {

    //=============================================================
    /**
     * Construct
     */
    //=============================================================
    private static Movie movie = new Movie();


    //=============================================================
    /**
     * 裁剪影片
     * @param srcPath     需要裁剪的原影片路徑
     * @param outPath     裁剪後的影片輸出路徑
     * @param startTimeMs 裁剪的起始時間
     * @param endTimeMs   裁剪的結束時間
     */
    //=============================================================
    public static String clipVideo(String srcPath, String outPath, long startTimeMs, long endTimeMs){

        try {
            movie = MovieCreator.build(srcPath);
        } catch (NullPointerException e) {
            if (!nullPointerExceptionHandle(srcPath)){
                return EMPTY;
            }

        } catch (IOException e) {
            errorCallbackHandle(RecordErrorType.CUT_VIDEO_ERROR.getType());
            return EMPTY;
        }


        List<Track> tracks = movie.getTracks();
        //移除舊的track
        movie.setTracks(new LinkedList<Track>());
        //處理的時間以秒為單位
        double startTime = startTimeMs / 1000;
        double endTime = endTimeMs / 1000;
        //計算剪切時間，視頻的採樣間隔大，以影片為準

        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                startTime = correctTimeToSyncSample(track, startTime, false);
                endTime = correctTimeToSyncSample(track, endTime, true);
                if (track.getHandler().equals("vide")) {
                    break;
                }
            }
        }
//        Log.d(TAG, "--->>>>startTime = " + startTime + "\n endTime = " + endTime);

        long currentSample;
        double currentTime;
        double lastTime;
        long startSample1;
        long endSample1;
        long delta;

        for (Track track : tracks) {
            currentSample = 0;
            currentTime = 0;  // 影片的時間長度
            lastTime = -1;   //上次擷取的最後時間
            startSample1 = -1;  //擷取開始時間
            endSample1 = -1;  //擷取結束時間

            //根據起始時間和截止時間獲取起始sample和截止sample的位置
            for (int i = 0; i < track.getSampleDurations().length; i++) {
                delta = track.getSampleDurations()[i];
                if (currentTime > lastTime && currentTime <= startTime) {
                    startSample1 = currentSample;  //編輯開始的時間
                }
                if (currentTime > lastTime && currentTime <= endTime) {
                    endSample1 = currentSample;  //編輯結束的時間
                }
                lastTime = currentTime;
                currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale(); //上次截取到的時間（避免在影片最後位置了還在增加編輯結束的時間)
                currentSample++;
            }
            if (startSample1 <= 0 && endSample1 <= 0) {
                errorCallbackHandle(RecordErrorType.CUT_VIDEO_ERROR.getType());
            }
            movie.addTrack(new CroppedTrack(track, startSample1, endSample1));// 添加截取的track
        }

        // 剪切後合成視頻mp4 //
        try {
            Container out = new DefaultMp4Builder().build(movie);
            FileOutputStream fos = new FileOutputStream(outPath);
            FileChannel fco = fos.getChannel();
            out.writeContainer(fco);
            fco.close();
            fos.close();
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            errorCallbackHandle(RecordErrorType.CUT_VIDEO_ERROR.getType());
        }

        return outPath;
    }


    //=============================================================
    /**
     * 換算剪切時間
     */
    //=============================================================
    public static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];
            int index = Arrays.binarySearch(track.getSyncSamples(), currentSample + 1);
            if (index >= 0) {
                timeOfSyncSamples[index] = currentTime;
            }
            currentTime += ((double) delta / (double) track.getTrackMetaData().getTimescale());
            currentSample++;
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }

    //=============================================================
    /**
     * RuntimeException Handle
     */
    //=============================================================
    private static boolean nullPointerExceptionHandle(String srcPath) {
        FileDataSourceImpl fileDataSource = null;
        try {
            fileDataSource = new FileDataSourceImpl(new File(srcPath));
            IsoFile isoFile = new IsoFile(fileDataSource);
            List<TrackBox> trackBoxes = isoFile.getBoxes(TrackBox.class);
            for (TrackBox trackBox : trackBoxes) {
                SchemeTypeBox schm = Path.getPath(trackBox, "mdia[0]/minf[0]/stbl[0]/stsd[0]/enc.[0]/sinf[0]/schm[0]");
                if (schm != null && (schm.getSchemeType().equals("cenc") || schm.getSchemeType().equals("cbc1"))) {
                    movie.addTrack(new CencMp4TrackImplImpl(fileDataSource + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
                } else {
                    movie.addTrack(new Mp4TrackImpl(fileDataSource + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
                }
            }
        } catch (IOException e) {
            errorCallbackHandle(RecordErrorType.CUT_VIDEO_ERROR.getType());
            return false;
        }
        return true;
    }
}
