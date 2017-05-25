package cn.ycoder.android.library.tool;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 系统计数器
 *
 * @author 启研
 * @created at 2017/4/25 10:53
 */

public class SysCounter {
    /**
     * 更新系统时间（秒）
     */
    private final static long INTERVAL_UPDATE_TIME = 10 * 60;
    /**
     * 时间戳
     */
    private long mTimeStamp = System.currentTimeMillis();
    /**
     * 剩余调用(onNeedUpdateTime)时间
     */
    private long mRestTime = 0;
    private OnSysCounterListener mListener;
    private Disposable mDisposable;

    private static SysCounter counter;

    /**
     * 安装
     *
     * @param l
     */
    public static void install(OnSysCounterListener l) {
        install(-1, l);
    }

    /**
     * 安装
     *
     * @param timeStamp 当前时间，小于=0则采用System.currentTimeMillis()
     * @param l
     */
    private static void install(long timeStamp, OnSysCounterListener l) {
        if (counter == null) {
            synchronized (ScreenInfo.class) {
                if (counter == null)
                    counter = new SysCounter(timeStamp, l);
            }
        }
    }

    /**
     * 重置计数器
     *
     * @param timeStamp 当前时间，小于=0则采用System.currentTimeMillis()
     * @param l
     */
    public static void reset(long timeStamp, OnSysCounterListener l) {
        synchronized (ScreenInfo.class) {
            if (counter != null)
                counter.destroy();
            counter = null;
        }
        install(timeStamp, l);
    }

    private SysCounter(long timeStamp, OnSysCounterListener l) {
        this.mListener = l;
        if (timeStamp > 0) {
            this.mTimeStamp = timeStamp;
        }
        this.mDisposable = Observable.interval(1, 1, TimeUnit.SECONDS).map(new Function<Long, Long>() {
            @Override
            public Long apply(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                return 1L;
            }
        }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                mRestTime -= aLong;
                if (mRestTime <= 0) {
                    mRestTime = INTERVAL_UPDATE_TIME;
                    mListener.onNeedUpdateTime(counter);
                }
                mTimeStamp += aLong * 1000;
            }
        });
    }

    /**
     * 销毁计数器
     */
    private void destroy() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
    }

    /**
     * 校正偏移量
     *
     * @param timeStamp
     */
    public void correctOffset(long timeStamp) {
        this.mTimeStamp = timeStamp;
    }

    /**
     * 获得时间戳
     *
     * @return
     */
    public static long getTimeStamp() {
        if (counter != null)
            return counter.mTimeStamp;
        else
            return System.currentTimeMillis();
    }

    public interface OnSysCounterListener {
        void onNeedUpdateTime(SysCounter s);
    }
}
