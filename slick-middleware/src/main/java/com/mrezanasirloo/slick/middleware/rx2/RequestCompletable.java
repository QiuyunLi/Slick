/*
 * Copyright 2018. M. Reza Nasirloo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mrezanasirloo.slick.middleware.rx2;


import com.mrezanasirloo.slick.middleware.BundleSlick;
import com.mrezanasirloo.slick.middleware.Middleware;
import com.mrezanasirloo.slick.middleware.Request;

import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 *         Created on: 2017-03-13
 */

public abstract class RequestCompletable<P> extends Request<P> {
    private CompletableObserver source;

    public abstract Completable target(P data);

    public RequestCompletable<P> with(P data) {
        this.data = data;
        if (!(data instanceof BundleSlick)) {
            bundleSlick = new BundleSlick().putParameter(data);
        }
        return this;
    }

    public RequestCompletable<P> through(Middleware... middleware) {
        this.middleware = Arrays.asList(middleware);
        Collections.reverse(this.middleware);
        return this;
    }

    @Override
    public void next() {
        if (!hasPassed()) {
            return;
        }

        if (this != routerStack.pop()) throw new AssertionError();
        final Completable response = target(data);
        if (source != null) {
            response.subscribe(source);
        }
        tooLateAlreadyFinished = true;
    }

    public void destination(CompletableObserver source) {
        this.source = source;
    }
}
