package com.carlospontual.twitch.list;

import com.carlospontual.twitch.list.helpers.DaggerTestInjectionHelper_TestAppComponent;
import com.carlospontual.twitch.list.helpers.TestInjectionHelper;

/**
 * Created by ccastro on 4/6/16.
 */
public class TestTwitchTopGames extends TwitchTopGames {

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerTestInjectionHelper_TestAppComponent.builder()
                .mockAppModule(new TestInjectionHelper.MockAppModule(this))
                .build();
    }
}
