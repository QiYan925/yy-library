package cn.ycoder.android.library.tool;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * @author 启研
 * @created at 2017/4/28 16:16
 */

public class ErrorHandlers {

  private static final String TAG = ErrorHandlers.class.getSimpleName();

  public static String displayError(Context context, Throwable throwable) {
    if (context == null) {
      Log.e(TAG, "[300] " + "Context is null");
      return "";
    }

    String errorMessage = null;
    if (throwable instanceof HttpException) {
      HttpException httpException = (HttpException) throwable;
      errorMessage = errorMessage(httpException);
    } else if (isNetWorkError(throwable)) {
      errorMessage = "网络未连接或不可用，请检查后重试";
    }
    if (errorMessage != null) {
    } else {
      Log.e(TAG, "[301]", throwable);
      errorMessage = errorMessage(throwable);
    }
    ToastUtils.showLong(errorMessage);
    return errorMessage;
  }


  public static Consumer<Throwable> displayErrorConsumer(final Context context) {
    return new Consumer<Throwable>() {
      @Override
      public void accept(@NonNull Throwable throwable) {
        displayError(context, throwable);
      }
    };
  }


  public static String errorMessage(Throwable throwable) {
    try {
      if (throwable instanceof HttpException) {
        BufferedReader in = new BufferedReader(((HttpException) throwable).response()
            .errorBody().charStream());
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
          buffer.append(line);
        }
        return buffer.toString();
      } else if (throwable instanceof OnErrorNotImplementedException) {
        return "网络请求异常，请稍后重试";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return throwable.getMessage();
  }

  /**
   * 判读是否网络异常
   */
  public static boolean isNetWorkError(Throwable throwable) {
    return throwable instanceof UnknownHostException ||
        throwable instanceof SocketTimeoutException ||
        (throwable.getCause() != null && throwable.getCause() instanceof SocketTimeoutException);
  }
}
