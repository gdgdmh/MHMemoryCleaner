package jp.mh.mhmemorycleaner;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.mh.MhMemoryUtil;
import jp.mh.MhUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  // 3G
  private static int kMaxFirstAllocMemory = 1024 * 1024 * 1024;
  private static int kMaxAllocLoop = 10; // 最大ループ回数

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    MhUtil.Print("MHMemoryCleaner::MainActivity::onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity_layout);

    Button button = (Button)findViewById(R.id.button_clear);
    button.setOnClickListener(this);
        /*new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MhUtil.Print("button_clear click");
        AllocMemoryAndDelete(MhMemoryUtil.GetAvailSystemMemoryByte(MhMemoryUtil.GetActivityManager()));
      }
    });
    */
    long memory = MhMemoryUtil.GetTotalMemoryByte(MhMemoryUtil.GetActivityManager(this));
    MhUtil.Print("memory " + memory + "byte");
    //int[] memory_array = MhMemoryUtil.GetMemory(memory);
    //MhUtil.Print("memory " + memory_array[3] + "," + memory_array[2] + "," + memory_array[1] + "," + memory_array[0] + "byte");
    MhUtil.Print("total memory " + MhMemoryUtil.GetTotalMemoryMegabyte(MhMemoryUtil.GetActivityManager(this)));
    MhUtil.Print("current memory " + MhMemoryUtil.GetAvailSystemMemoryMegabyte(MhMemoryUtil.GetActivityManager(this)));

    SetTotalMemoryText(MhMemoryUtil.GetTotalMemoryMegabyte(MhMemoryUtil.GetActivityManager(this)));
    SetCurrentMemoryText(MhMemoryUtil.GetAvailSystemMemoryMegabyte(MhMemoryUtil.GetActivityManager(this)));
    //System.gc();
  }
  /*
    Button button = (Button)findViewById(R.id.refresh_button);
    button.setOnClickListener(this);
    reflesh_button_ = button;

   */

  // View.OnClickListener
  @Override
  public void onClick(View v) {
    MhUtil.Print("MainActivity::onCreate click");
    ActivityManager activity_manager = MhMemoryUtil.GetActivityManager(this);
    // メモリを開放
    AllocMemoryAndDelete(MhMemoryUtil.GetAvailSystemMemoryByte(activity_manager));
    // テキストを更新
    SetTotalMemoryText(MhMemoryUtil.GetTotalMemoryMegabyte(activity_manager));
    SetCurrentMemoryText(MhMemoryUtil.GetAvailSystemMemoryMegabyte(activity_manager));
  }

  private void AllocMemoryAndDelete(long memory_num) {

    // 確保するメモリを入れる配列
    char memory_array[][] = new char [kMaxAllocLoop][];
    try {

      int alloc_memory = 0;
      if (memory_num > kMaxFirstAllocMemory) {
        // 指定された値がメモリ確保最大値を超えてしまっているので
        // メモリ確保指定最大値に設定
        alloc_memory = kMaxFirstAllocMemory;
      } else {
        // 指定された値をそのまま使う
        alloc_memory = (int) (memory_num);
      }

      alloc_memory = 1024 * 1024 * 100;

      boolean loop = true; // ループフラグ
      boolean alloc_success = false; // メモリ確保が成功したか
      int count = 0; // ループのカウント

      while (loop == true) {
        boolean memory_error = false;
        try {
          // メモリ確保をしてみる
          memory_array[count] = new char[alloc_memory];
          alloc_success = true;
          MhUtil.Print("MainActivity::AllocMemoryAndDelete alloc success " + count);
        } catch (OutOfMemoryError outOfMemoryError2) {
          memory_error = true;
          MhUtil.Print("MainActivity::AllocMemoryAndDelete loop out of memory " + count);
        } catch (Exception e) {
          MhUtil.Print("MainActivity::AllocMemoryAndDelete loop e " + e);
        }
        // カウントを増やす,小さいメモリを確保する
        ++count;
        alloc_memory = alloc_memory - (alloc_memory / 10);
        //alloc_memory = (alloc_memory / 2);

        // 規定回数以上のループ
        if ((count >= kMaxAllocLoop) ||
            // メモリ確保が一回以上成功してからのメモリ確保エラー
            ((memory_error == true) && (alloc_success == true))) {
          // 規定ループ回数以上はやらない
          loop = false;
        }
      }
    } catch (OutOfMemoryError outOfMemoryError) {
      MhUtil.Print("MainActivity::AllocMemoryAndDelete out of memory ");
    } catch (Exception e) {
      MhUtil.Print("MainActivity::AllocMemoryAndDelete e " + e);
    }

    // ループ内で確保したメモリを開放
    int size = memory_array.length;
    for (int i = 0; i < size; ++i) {
      memory_array[i] = null;
    }
    memory_array = null;

    // GCを起動してメモリ解放
    System.gc();
  }

  private void SetTotalMemoryText(long memory) {
    TextView tv = (TextView)findViewById(R.id.total_memory_text);
    tv.setText("TotalMemory " + memory + "MByte");
  }

  private void SetCurrentMemoryText(long memory) {
    TextView tv = (TextView)findViewById(R.id.current_memory_text);
    tv.setText("CurrentMemory " + memory + "MByte");
  }

}
