package com.ccladner.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

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

import java.io.File;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PATH_LAYOUT_PARAMS.setMargins(10, 5, 10, 5);
        FILE_DATA_LABEL_LAYOUT_PARAMS.rightMargin = 32;
        IMAGE_LAYOUT_PARAMS.gravity = Gravity.CENTER_VERTICAL;
        IMAGE_LAYOUT_PARAMS.setMargins(5, 0, 10, 0);

        displayUpdate();
    }

    private void displayUpdate(){
        ScrollView scrvFileParent = findViewById(R.id.fileScrollViewParent);
        scrvFileParent.removeAllViews();
        scrvFileParent.scrollTo(0, 0);
        LinearLayoutCompat lnvFileParent = new LinearLayoutCompat(this);
        lnvFileParent.setOrientation(LinearLayoutCompat.VERTICAL);
        lnvFileParent.addView(genPathScrollView());
        scrvFileParent.addView(lnvFileParent);

        if(currentDirectory == null){
            Log.i("AppTest", Environment.getExternalStorageDirectory().getAbsolutePath());
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

            lnvFileInfoContainer.addView(gridSpaceData, FILE_SPACE_CONTAINER_LAYOUT_PARAMS);
            lnvFileContainer.addView(lnvFileInfoContainer);
            lnvFileParent.addView(lnvFileContainer);

        }
    }

    private ScrollView genPathScrollView(){
        ScrollView scrvPath = new ScrollView(this);
        LinearLayoutCompat lnvPath = new LinearLayoutCompat(this);
        lnvPath.setOrientation(LinearLayoutCompat.HORIZONTAL);
        if(currentDirectory == null){
            TextView tvPath = new TextView(this);
            tvPath.setTextSize(TypedValue.COMPLEX_UNIT_SP, PATH_FONT_SIZE);
            tvPath.setText(R.string.path_home);
            lnvPath.addView(tvPath, PATH_LAYOUT_PARAMS);
            scrvPath.addView(lnvPath);
            return scrvPath;
        }
        else{
            //Loop through previous directory to create path scroll bar
            return scrvPath;
        }
    }

}
