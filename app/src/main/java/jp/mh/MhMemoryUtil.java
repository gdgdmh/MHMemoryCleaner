package jp.mh;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by mh on 2016/06/12.
 */
public class MhMemoryUtil {
  /*

ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
ActivityManager am = ((ActivityManager)mContext.getSystemService(Activity.ACTIVITY_SERVICE));
am.getMemoryInfo(info);
Log.d("LinuxHeap", "total[KB] = " + info.totalMem/1024); // API Level16以上で取得できる属性
Log.d(TAG, "availabled[KB] = " + info.availMem/1024);
   */

  static public ActivityManager GetActivityManager(Context context) {
    return (ActivityManager)context.getSystemService(Activity.ACTIVITY_SERVICE);
  }

  static public long GetTotalMemoryByte(ActivityManager activity_manager) {
    long memory_value = 0;
    try {
      ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
      activity_manager.getMemoryInfo(info);
      memory_value = info.totalMem;
    } catch (OutOfMemoryError outOfMemoryError) {
      MhUtil.Print("MhMemoryUtil::GetTotalMemoryByte() outOfMemoryError " + outOfMemoryError);
    } catch (Exception e) {
      MhUtil.Print("MhMemoryUtil::GetTotalMemoryByte() e " + e);
    }
    return memory_value;
  }

  static public long GetTotalMemoryKilobyte(ActivityManager activity_manager) {
    return GetTotalMemoryByte(activity_manager) / 1024;
  }

  static public long GetTotalMemoryMegabyte(ActivityManager activity_manager) {
    return (GetTotalMemoryByte(activity_manager) / 1024) / 1024;
  }

  static public long GetAvailSystemMemoryByte(ActivityManager activity_manager) {
    long memory_value = 0;
    try {
      ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
      activity_manager.getMemoryInfo(info);
      memory_value = info.availMem;
    } catch (OutOfMemoryError outOfMemoryError) {
      MhUtil.Print("MhMemoryUtil::GetAvailSystemMemoryByte() outOfMemoryError " + outOfMemoryError);
    } catch (Exception e) {
      MhUtil.Print("MhMemoryUtil::GetAvailSystemMemoryByte() e " + e);
    }
    return memory_value;
  }

  static public long GetAvailSystemMemoryMegabyte(ActivityManager activity_manager) {
    return (GetAvailSystemMemoryByte(activity_manager) / 1024) / 1024;
  }

  /**
   * メモリサイズを返す
   * @return int[] [0]バイト [1]キロバイト [2]メガバイト [3]ギガバイト
   */
  static public int[] GetMemory(long memory) {
    int[] memory_array = null;
    try {
      memory_array = new int[4];
      long m = memory;
      memory_array[3] = (int)(m / (1024 * 1024 * 1024));
      m -= memory_array[3] * (1024 * 1024 * 1024);
      memory_array[2] = (int)(m / (1024 * 1024));
      m -= memory_array[2] * (1024 * 1024);
      memory_array[1] = (int)(m / 1024);
      m -= memory_array[1] * 1024;
      memory_array[0] = (int)(m);
    } catch (OutOfMemoryError outOfMemoryError) {
      memory_array = null;
    }
    return memory_array;
  }

  // テキストで返す

}
