/*
 * Copyright 2018 The Android Open Source Project
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

package androidx.media2.test.service.tests;

import static androidx.media2.test.common.CommonConstants.DEFAULT_TEST_NAME;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.media2.test.service.RemoteMediaControllerCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.filters.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** Test {@link RemoteMediaControllerCompat}. */
@SdkSuppress(maxSdkVersion = 32) // b/244312419
@RunWith(AndroidJUnit4.class)
public class RemoteMediaControllerCompatTest extends MediaSessionTestBase {
    private Context mContext;
    private MediaSessionCompat mSessionCompat;
    private RemoteMediaControllerCompat mRemoteControllerCompat;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        sHandler.postAndSync(new Runnable() {
            @Override
            public void run() {
                mSessionCompat = new MediaSessionCompat(mContext, DEFAULT_TEST_NAME);
                mSessionCompat.setActive(true);
            }
        });
        mRemoteControllerCompat = new RemoteMediaControllerCompat(
                mContext, mSessionCompat.getSessionToken(), true /* waitForConnection */);
    }

    @After
    public void cleanUp() {
        mSessionCompat.release();
        mRemoteControllerCompat.cleanUp();
    }

    @Test
    @SmallTest
    public void play() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        mSessionCompat.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                latch.countDown();
            }
        }, sHandler);

        mRemoteControllerCompat.getTransportControls().play();
        assertTrue(latch.await(TIMEOUT_MS, TimeUnit.MILLISECONDS));
    }
}
