/*
 * Copyright 2023 The Android Open Source Project
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

package androidx.camera.testing.impl.fakes;

import static androidx.camera.core.impl.UseCaseConfig.OPTION_CAPTURE_CONFIG_UNPACKER;
import static androidx.camera.core.impl.UseCaseConfig.OPTION_SESSION_CONFIG_UNPACKER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.camera.core.ImageCapture.CaptureMode;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.UseCaseConfigFactory;

/**
 * A fake implementation of {@link UseCaseConfigFactory}.
 */
@RequiresApi(21) // TODO(b/200306659): Remove and replace with annotation on package-info.java
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class FakeUseCaseConfigFactory implements UseCaseConfigFactory {

    @NonNull
    private CaptureType mLastRequestedCaptureType;

    /**
     * Returns the configuration for the given capture type, or <code>null</code> if the
     * configuration cannot be produced.
     */
    @Nullable
    @Override
    public Config getConfig(
            @NonNull CaptureType captureType,
            @CaptureMode int captureMode) {
        mLastRequestedCaptureType = captureType;
        MutableOptionsBundle mutableConfig = MutableOptionsBundle.create();

        mutableConfig.insertOption(OPTION_CAPTURE_CONFIG_UNPACKER, (config, builder) -> {});
        mutableConfig.insertOption(OPTION_SESSION_CONFIG_UNPACKER,
                (resolution, config, builder) -> {});

        return OptionsBundle.from(mutableConfig);
    }

    @NonNull
    public CaptureType getLastRequestedCaptureType() {
        return mLastRequestedCaptureType;
    }
}
