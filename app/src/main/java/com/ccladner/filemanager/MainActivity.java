package com.ccladner.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private File currentDirectory = null;
    private ArrayList<File> previousDirectories = new ArrayList<>();

    private final float SECTION_LABEL_FONT_SIZE = 32;
    private final float PATH_FONT_SIZE = 16;
    private final float FILE_NAME_FONT_SIZE = 26;
    private final float FILE_DATA_FONT_SIZE = 13;
    private final LinearLayoutCompat.LayoutParams PATH_LAYOUT_PARAMS = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
    private final LinearLayoutCompat.LayoutParams IMAGE_LAYOUT_PARAMS = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
    private final LinearLayoutCompat.LayoutParams FILE_DATA_LABEL_LAYOUT_PARAMS = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
    private final LinearLayoutCompat.LayoutParams FILE_DATA_LAYOUT_PARAMS = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
    private final LinearLayoutCompat.LayoutParams FILE_SPACE_CONTAINER_LAYOUT_PARAMS = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private final static String[] REQUIRED_PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    protected void checkPermissions(){
        final List<String> missingPermissions = new ArrayList<>();
        for (final String permission: REQUIRED_PERMISSIONS){
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if(result != PackageManager.PERMISSION_GRANTED){
                missingPermissions.add(permission);
            }
        }
        if(!missingPermissions.isEmpty()){
            final String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        }
        else{
            final int[] grantResults = new int[REQUIRED_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_PERMISSIONS, grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                for(int index = 0; index < permissions.length; index++){
                    if(grantResults[index] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Required permission '" + permissions[index] + "' not granted, exiting app", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermissions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PATH_LAYOUT_PARAMS.setMargins(10, 5, 10, 5);
        FILE_DATA_LABEL_LAYOUT_PARAMS.rightMargin = 32;
        IMAGE_LAYOUT_PARAMS.gravity = Gravity.CENTER_VERTICAL;
        IMAGE_LAYOUT_PARAMS.setMargins(5, 0, 10, 0);

        displayUpdate();
    }

    private void displayUpdate(){
        ScrollView scrvFileParent = findViewById(R.id.scrvFileParent);
        genPathScrollView();
        scrvFileParent.removeAllViews();
        scrvFileParent.scrollTo(0, 0);
        LinearLayoutCompat lnvFileParent = new LinearLayoutCompat(this);
        lnvFileParent.setOrientation(LinearLayoutCompat.VERTICAL);
        scrvFileParent.addView(lnvFileParent);

        if(currentDirectory == null){
            Log.i("AppTest Root", Environment.getExternalStorageDirectory().getAbsolutePath());
            File externalStorageRootDir = Environment.getExternalStorageDirectory();

            LinearLayoutCompat lnvFileContainer = new LinearLayoutCompat(this);
            LinearLayoutCompat lnvFileInfoContainer = new LinearLayoutCompat(this);
            lnvFileInfoContainer.setOrientation(LinearLayoutCompat.VERTICAL);
            GridLayout gridSpaceData = new GridLayout(this);
            gridSpaceData.setColumnCount(2);
            gridSpaceData.setRowCount(2);

            TextView tvSectionStorageLabel = new TextView(this);
            tvSectionStorageLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, SECTION_LABEL_FONT_SIZE);
            tvSectionStorageLabel.setText(R.string.section_storage);
            lnvFileParent.addView(tvSectionStorageLabel);

            ImageView ivIcon = new ImageView(this);
            ivIcon.setImageResource(R.drawable.place_holder_64x64);
            lnvFileContainer.addView(ivIcon, IMAGE_LAYOUT_PARAMS);

            TextView tvFileName = new TextView(this);
            tvFileName.setText(R.string.on_board_storage);
            tvFileName.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_NAME_FONT_SIZE);
            lnvFileInfoContainer.addView(tvFileName);

            TextView tvTotalSpaceLabel = new TextView(this);
            tvTotalSpaceLabel.setText(R.string.total_space);
            tvTotalSpaceLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_DATA_FONT_SIZE);
            gridSpaceData.addView(tvTotalSpaceLabel, FILE_DATA_LABEL_LAYOUT_PARAMS);

            TextView tvTotalSpace = new TextView(this);
            tvTotalSpace.setText(String.valueOf(externalStorageRootDir.getTotalSpace()));
            tvTotalSpace.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_DATA_FONT_SIZE);
            gridSpaceData.addView(tvTotalSpace, FILE_DATA_LAYOUT_PARAMS);

            TextView tvFreeSpaceLabel = new TextView(this);
            tvFreeSpaceLabel.setText(R.string.free_space);
            tvFreeSpaceLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_DATA_FONT_SIZE);
            gridSpaceData.addView(tvFreeSpaceLabel,FILE_DATA_LABEL_LAYOUT_PARAMS);

            TextView tvFreeSpace = new TextView(this);
            tvFreeSpace.setText(String.valueOf(externalStorageRootDir.getFreeSpace()));
            tvFreeSpace.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_DATA_FONT_SIZE);
            gridSpaceData.addView(tvFreeSpace, FILE_DATA_LAYOUT_PARAMS);

            lnvFileContainer.setOnClickListener(new HandleSelect(externalStorageRootDir));

            lnvFileInfoContainer.addView(gridSpaceData, FILE_SPACE_CONTAINER_LAYOUT_PARAMS);
            lnvFileContainer.addView(lnvFileInfoContainer);
            lnvFileParent.addView(lnvFileContainer);
            return;
        }

        Log.i("AppTest Dir", this.currentDirectory.getAbsolutePath());

        for(File file : this.currentDirectory.listFiles()){
            LinearLayoutCompat lnvFileContainer = new LinearLayoutCompat(this);
            LinearLayoutCompat lnvFileInfoContainer = new LinearLayoutCompat(this);
            lnvFileInfoContainer.setOrientation(LinearLayoutCompat.VERTICAL);
            GridLayout gridSpaceData = new GridLayout(this);
            gridSpaceData.setColumnCount(2);
            gridSpaceData.setRowCount(2);

            ImageView ivIcon = new ImageView(this);
            ivIcon.setImageResource(R.drawable.place_holder_64x64);
            lnvFileContainer.addView(ivIcon, IMAGE_LAYOUT_PARAMS);

            TextView tvFileName = new TextView(this);
            tvFileName.setText(file.getName());
            tvFileName.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_NAME_FONT_SIZE);
            lnvFileInfoContainer.addView(tvFileName);

            if(file.isDirectory()) {
                TextView tvDirFileCountLabel = new TextView(this);
                tvDirFileCountLabel.setText(R.string.dir_file_count);
                tvDirFileCountLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_DATA_FONT_SIZE);
                gridSpaceData.addView(tvDirFileCountLabel, FILE_DATA_LABEL_LAYOUT_PARAMS);

                TextView tvDirFileCount = new TextView(this);
                tvDirFileCount.setText(String.valueOf(file.listFiles().length));
                tvDirFileCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_DATA_FONT_SIZE);
                gridSpaceData.addView(tvDirFileCount, FILE_DATA_LAYOUT_PARAMS);
            }
            else{
                TextView tvFileSizeLabel = new TextView(this);
                tvFileSizeLabel.setText(R.string.file_size);
                tvFileSizeLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_DATA_FONT_SIZE);
                gridSpaceData.addView(tvFileSizeLabel, FILE_DATA_LABEL_LAYOUT_PARAMS);

                TextView tvFileSize = new TextView(this);
                tvFileSize.setText(String.valueOf(file.length()));
                tvFileSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, FILE_DATA_FONT_SIZE);
                gridSpaceData.addView(tvFileSize, FILE_DATA_LAYOUT_PARAMS);
            }

            lnvFileContainer.setOnClickListener(new HandleSelect(file));

            lnvFileInfoContainer.addView(gridSpaceData, FILE_SPACE_CONTAINER_LAYOUT_PARAMS);
            lnvFileContainer.addView(lnvFileInfoContainer);
            lnvFileParent.addView(lnvFileContainer);
        }

    }

    private void genPathScrollView(){
        ScrollView scrvPath = findViewById(R.id.scrvPath);
        scrvPath.removeAllViews();
        LinearLayoutCompat lnvPath = new LinearLayoutCompat(this);
        lnvPath.setOrientation(LinearLayoutCompat.HORIZONTAL);
        if(this.currentDirectory == null){
            TextView tvPath = new TextView(this);
            tvPath.setTextSize(TypedValue.COMPLEX_UNIT_SP, PATH_FONT_SIZE);
            tvPath.setText(R.string.path_home);
            lnvPath.addView(tvPath, PATH_LAYOUT_PARAMS);
            scrvPath.addView(lnvPath);
        }
        else{
            //Loop through previous directories to create path
        }
    }

    private class HandleSelect implements View.OnClickListener{
        private File selectedFile;

        HandleSelect(File selectedFile){
            this.selectedFile = selectedFile;
        }

        @Override
        public void onClick(View view) {
            MainActivity.this.previousDirectories.add(MainActivity.this.currentDirectory);
            MainActivity.this.currentDirectory = this.selectedFile;
            displayUpdate();
        }
    }

}
