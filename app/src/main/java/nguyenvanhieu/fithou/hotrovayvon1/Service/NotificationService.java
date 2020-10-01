//package nguyenvanhieu.fithou.hotrovayvon1.Service;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
//import nguyenvanhieu.fithou.hotrovayvon1.Controller.BaiVietCuaToiActivity;
//import nguyenvanhieu.fithou.hotrovayvon1.R;
//
//import static android.app.Notification.DEFAULT_ALL;
//import static android.app.Notification.VISIBILITY_PUBLIC;
//import static android.content.Intent.getIntent;
//import static android.content.Intent.getIntentOld;
//
//public class NotificationService extends Service {
//    //@androidx.annotation.Nullable
//    Bitmap largeIcon;
//    DatabaseReference databaseReference;
//    NotificationCompat.Builder notiBuilder;
//    //String UserUID2;
//    private static final int MY_NOTIFICATION_ID = 12345;
//    private static final int MY_REQUEST_CODE = 100;
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.d("Service Thongbao","Create");
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        notiBuilder = new NotificationCompat.Builder(NotificationService.this);
//        CreateBitmap();
//    }
//
//    private void CreateBitmap() {
//        int height = 100;
//        int width = 100;
//        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.info3);
//        Bitmap b = bitmapdraw.getBitmap();
//        largeIcon = Bitmap.createScaledBitmap(b, width, height, false);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//       String UserUID2 = intent.getStringExtra("UserUID");
//        Log.e("userService",UserUID2);
//        Log.d("Service Thongbao","Start: UID: "+UserUID2);
//        if(UserUID2 != "" && UserUID2 !=null && databaseReference!=null) {
//            databaseReference.child("DanhSachPost").child(UserUID2).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.exists()) {
//                        String TrangThai = "";
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            baiDang bd = dataSnapshot1.getValue(baiDang.class);
//                            if (bd.getCheck()!=1 && bd.getNotified()== 0)
//                            {
//                                if (bd.getCheck() != 1) {
//                                    if (bd.getCheck() == 0) {
//                                        TrangThai = "không được duyệt vì dữ liệu không hợp lệ ";
//                                    }
//                                    if (bd.getCheck() == 2) {
//                                        TrangThai = "đã được duyệt";
//                                    }
//                                    String Content = "Bài viết này " + TrangThai;
//                                    if (notiBuilder == null) {
//                                        return;
//                                    }
//                                    notiBuilder.setAutoCancel(true);
//                                    notiBuilder.setVisibility(VISIBILITY_PUBLIC);
//                                    notiBuilder.setSmallIcon(R.mipmap.ic_launcher);
//                                    notiBuilder.setDefaults(DEFAULT_ALL);
//                                    notiBuilder.setPriority(Notification.PRIORITY_MAX);
//                                    notiBuilder.setLargeIcon(largeIcon);
//                                    notiBuilder.setTicker("This is a ticker");
//                                    notiBuilder.setWhen(System.currentTimeMillis() + 10 * 1000);
//                                    notiBuilder.setContentTitle("Thông tin bài viết : " + bd.getId());
//                                    notiBuilder.setContentText(Content);
//                                    Intent intent = new Intent(NotificationService.this, BaiVietCuaToiActivity.class);
//                                    PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, MY_REQUEST_CODE,
//                                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                    notiBuilder.setContentIntent(pendingIntent);
//                                    NotificationManager notificationService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                                    Notification notification = notiBuilder.build();
//                                    notificationService.notify(MY_NOTIFICATION_ID, notification);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//       // return super.onStartCommand(intent, flags, startId);
//        return startId;
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d("Service Thongbao", "Destroyed");
//        databaseReference = null;
//        notiBuilder = null;
//        super.stopSelf();
//        super.onDestroy();
//    }
//}
