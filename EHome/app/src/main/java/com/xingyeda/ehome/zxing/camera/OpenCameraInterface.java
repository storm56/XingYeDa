/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xingyeda.ehome.zxing.camera;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.util.Log;

public final class OpenCameraInterface {

  private static final String TAG = OpenCameraInterface.class.getName();

  private OpenCameraInterface() {
  }

  
  /**
   * Opens the requested Camera with {@link Camera#open(int)}, if one exists.
   *
   * @param cameraId Camera ID of the Camera to use. A negative value means "no preference"
   * @return handle to {@link Camera} that was opened
   */
  @SuppressLint("NewApi")
public static Camera open(int cameraId) {
    
    int numCameras = Camera.getNumberOfCameras();
    if (numCameras == 0) {
      Log.w(TAG, "No cameras!");
      return null;
    }

    boolean explicitRequest = cameraId >= 0;

    if (!explicitRequest) {
      // Select a Camera if no explicit Camera requested
      int index = 0;
      while (index < numCameras) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(index, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
          break;
        }
        index++;
      }
      
      cameraId = index;
    }

    Camera camera;
    if (cameraId < numCameras) {
      Log.i(TAG, "Opening Camera #" + cameraId);
      camera = Camera.open(cameraId);
    } else {
      if (explicitRequest) {
        Log.w(TAG, "Requested Camera does not exist: " + cameraId);
        camera = null;
      } else {
        Log.i(TAG, "No Camera facing back; returning Camera #0");
        camera = Camera.open(0);
      }
    }
    
    return camera;
  }
  
  
  /**
   * Opens a rear-facing Camera with {@link Camera#open(int)}, if one exists, or opens Camera 0.
   *
   * @return handle to {@link Camera} that was opened
   */
  public static Camera open() {
    return open(-1);
  }

}
