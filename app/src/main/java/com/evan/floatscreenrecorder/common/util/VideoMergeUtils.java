package com.evan.floatscreenrecorder.common.util;

import static com.evan.floatscreenrecorder.record.service.RecordingService.errorCallbackHandle;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.SchemeTypeBox;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.CencMp4TrackImplImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Mp4TrackImpl;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.util.Path;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.evan.floatscreenrecorder.common.constant.RecordErrorType;

/**
 * Created by Evan on 2021/7/7.
 * <p>
 * Description：
 */
public class VideoMergeUtils {

    //=============================================================
    /**
     * Construct
     */
    //=============================================================
    private final static String PREFIX_VIDEO_HANDLER = "vide";
    private final static String PREFIX_AUDIO_HANDLER = "soun";
    private static Movie movie = new Movie();


    //=============================================================
    /**
     * 對Mp4文件集合進行合併（按照順序一個一個一接起來）
     * @param mp4PathList 想要被合成的影片列表，List<String>
     * @param outPutPath  [輸出]合成後影片的輸出絕對路徑 ()
     */
    //=============================================================
    public static void mergeMp4List(List<String> mp4PathList, String outPutPath) {

        List<Movie> mp4MovieList = new ArrayList<>();// Movie對象集合[輸入的影片]
        for (String mp4Path : mp4PathList) { // 將每個轉成Movie對象
            try {
                movie = MovieCreator.build(mp4Path);
            } catch (NullPointerException e) {
                if (!nullPointerExceptionHandle(mp4Path)){
                    return;
                }

            } catch (IOException e) {
                errorCallbackHandle(RecordErrorType.MERGE_ERROR.getType());
                return;
            }

            mp4MovieList.add(movie);

        }

        List<Track> audioTracks = new LinkedList<>();// 音頻通道集合
        List<Track> videoTracks = new LinkedList<>();// 影片通道集合

        for (Movie mp4Movie : mp4MovieList) {  // 對Movie對象集合進行循環
            for (Track inMovieTrack : mp4Movie.getTracks()) {
                if (PREFIX_AUDIO_HANDLER.equals(inMovieTrack.getHandler())) {// 從Movie對象中取出音音頻通道
                    audioTracks.add(inMovieTrack);
                }
                if (PREFIX_VIDEO_HANDLER.equals(inMovieTrack.getHandler())) {// 從Movie對象中取出影片通道
                    videoTracks.add(inMovieTrack);
                }
            }
        }


        try {
            Movie resultMovie = new Movie(); // 結果Movie對象[輸出]
            if (!audioTracks.isEmpty()) {
                audioTracks.size();// 將所有音頻通道合併
                resultMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[0])));
            }
            if (!videoTracks.isEmpty()) {
                videoTracks.size();// 將所有影片通道合併
                resultMovie.addTrack(new AppendTrack(videoTracks.toArray(new Track[0])));
            }

            Container outContainer = new DefaultMp4Builder().build(resultMovie); // 將結果Movie對象封裝成容器

            FileChannel fileChannel = null;
            try {
                fileChannel = new RandomAccessFile(outPutPath, "rwd").getChannel();
                outContainer.writeContainer(fileChannel); // 將內容寫入
            } catch (IOException e) {
                errorCallbackHandle(RecordErrorType.MERGE_ERROR.getType());
            } finally {
                if (fileChannel != null) {
                    try {
                        fileChannel.close();
                    } catch (IOException e) {
                        errorCallbackHandle(RecordErrorType.MERGE_ERROR.getType());
                    }
                }
            }


        } catch (IOException e) {
            errorCallbackHandle(RecordErrorType.MERGE_ERROR.getType());
        }
    }


    //=============================================================
    /**
     * RuntimeException Handle
     */
    //=============================================================
    private static boolean nullPointerExceptionHandle(String mp4Path) {
        FileDataSourceImpl fileDataSource = null;
        try {
            fileDataSource = new FileDataSourceImpl(new File(mp4Path));
            IsoFile isoFile = new IsoFile(fileDataSource);
            List<TrackBox> trackBoxes = isoFile.getBoxes(TrackBox.class);
            for (TrackBox trackBox : trackBoxes) {
                SchemeTypeBox schm = Path.getPath(trackBox, "mdia[0]/minf[0]/stbl[0]/stsd[0]/enc.[0]/sinf[0]/schm[0]");
                if (schm != null && (schm.getSchemeType().equals("cenc") || schm.getSchemeType().equals("cbc1"))) {
                    movie.addTrack(new CencMp4TrackImplImpl(fileDataSource.toString() + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
                } else {
                    movie.addTrack(new Mp4TrackImpl(fileDataSource.toString() + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
                }
            }
        } catch (IOException e) {
            errorCallbackHandle(RecordErrorType.MERGE_ERROR.getType());
            return false;
        }
        return true;
    }
}