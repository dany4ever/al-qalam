package com.uzislam.alqalam.arabicinstaller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class InstallerActivity extends Activity {
    private String rootDirectory = "";
    private InstallerActivity self;
    private static boolean sInstallationSuccess = false;
    private static boolean sIsInstalling = false;
    private final static Object sInstallerStateLock = new Object();
    public ProgressBar mProgress;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        rootDirectory = Environment.getExternalStorageDirectory() + "/";
        // only run the installer if there isn't another one running
        synchronized (sInstallerStateLock) {
            if (!sIsInstalling && !sInstallationSuccess) {
                sIsInstalling = true;
                runInstaller();
            }
        }
    }

    private void runInstaller() {
    	setContentView(R.layout.installing);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mProgress.setMax(6236); // 6236 is the total number of ayats (files)
    	try {
            Resources res = getResources();
            AssetFileDescriptor dataPackFd = res.openRawResourceFd(R.raw.arabicpack);
            InputStream stream = dataPackFd.createInputStream();

            (new Thread(new unzipper(stream))).start();
        } catch (IOException e) {
            Log.e("Al-Qalam", "Unable to open data pack resource.");
            e.printStackTrace();
        }
    }


    private boolean unzipDataPack(InputStream stream) {
        FileOutputStream out;
        byte buf[] = new byte[16384];
        int mProgressStatus = 0;
        try {
            ZipInputStream zis = new ZipInputStream(stream);
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                if (entry.isDirectory()) {
                    File newDir = new File(rootDirectory + entry.getName());
                    newDir.mkdir();
                } else {
                	String name = entry.getName();
                    File outputFile = new File(rootDirectory + name);
                    String outputPath = outputFile.getCanonicalPath();
                    name = outputPath
                            .substring(outputPath.lastIndexOf("/") + 1);
                    outputPath = outputPath.substring(0, outputPath
                            .lastIndexOf("/"));
                    File outputDir = new File(outputPath);
                    outputDir.mkdirs();
                    outputFile = new File(outputPath, name);
                    outputFile.createNewFile();
                    out = new FileOutputStream(outputFile);

                    // Set the progress
                    mProgress.setProgress(++mProgressStatus);

                    int numread = 0;
                    do {
                        numread = zis.read(buf);
                        if (numread <= 0) {
                            break;
                        } else {
                            out.write(buf, 0, numread);
                        }
                    } while (true);
                    out.close();
                }
                entry = zis.getNextEntry();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private class unzipper implements Runnable {
        public InputStream stream;

        public unzipper(InputStream is) {
            stream = is;
        }

        public void run() {
            boolean result = unzipDataPack(stream);
            synchronized (sInstallerStateLock) {
                sInstallationSuccess = result;
                sIsInstalling = false;
            }
            if (sInstallationSuccess) {
                // installation completed
                runOnUiThread(new installationSuccessful());
            } else {
                // installation failed
                // signal install error if the activity is finishing (can't ask the user to retry)
                if (self.isFinishing()) {
                    // TODO: Maybe a Toast message will suffice?
                } else {
                    // the activity is still running, ask the user to retry.
                    runOnUiThread(new retryDisplayer());
                }
            }
        }
    }
    
    public class installationSuccessful implements Runnable {
        public void run() {
            setContentView(R.layout.uninstall);
            Button uninstallButton = (Button) findViewById(R.id.uninstallButton);
            uninstallButton.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    // Show Android's native uninstaller activity
                    Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:com.uzislam.alqalam.arabicinstaller"));
                    startActivity(intent);
                    // And finish InstallerActivity
                    self.finish();
                }
            });
        }
    }


    public class retryDisplayer implements Runnable {
        public void run() {
            setContentView(R.layout.retry);
            Button retryButton = (Button) findViewById(R.id.retryButton);
            retryButton.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    // only run the installer if there isn't another one running
                    // (we only get here if the installer couldn't complete successfully before)
                    synchronized (sInstallerStateLock) {
                        if (!sIsInstalling) {
                            sIsInstalling = true;
                            runInstaller();
                        }
                    }
                }
            });
        }
    }

}

