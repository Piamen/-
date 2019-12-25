package edu.bit.mecoming.managers;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;

import edu.bit.mecoming.MainActivity;
import edu.bit.mecoming.algorithm.MainService;
import edu.bit.mecoming.algorithm.TodoEvent;


/**
 * @ class:  AlarmManager
 * @ brief:  开启、关闭响铃震动
 * @ author: 谢楚云
 */
public class AlarmManager {
    private MediaPlayer mp = new MediaPlayer();
    private Vibrator vibrator;
    public TodoEvent alarmEvent;

    public TodoEvent getAlarmEvent() {
        return alarmEvent;
    }

    private void createDialog(final Context context) {

    }
    public void startAlarm(TodoEvent e, Context context)
    {
        startMedia(context);
        startVibrator();
        Message message = new Message();
        MainActivity.Instance.createDialogHandler.sendMessage(new Message());
        alarmEvent = e;
    }
    public void stopAlarm()
    {
        mp.stop();
        vibrator.cancel();
    }

    private PowerManager.WakeLock mWakelock;
    private void startMedia(Context context) {
        try {

            mp.setDataSource(context,
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 震动
     */
    private void startVibrator() {
        /**
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         *
         */
        vibrator = (Vibrator) MainService.context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 500, 1000, 500, 1000 }; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 0);
    }


}
