package org.supremus.sych.sychnews;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateWork extends Worker {
    public UpdateWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        if (Build.VERSION.SDK_INT>25)
            context.startForegroundService(new Intent(context, UpdateService.class));
        else
            context.startService(new Intent(context, UpdateService.class));
        return Result.success();
    }
}
