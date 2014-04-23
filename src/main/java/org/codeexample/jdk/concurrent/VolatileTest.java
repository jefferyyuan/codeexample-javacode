/*
 * Copyright 2014 administrator.
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
package org.codeexample.jdk.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VolatileTest {

    private static final Logger LOGGER = Logger.getLogger(VolatileTest.class.getName());

    private static //volatile 
            int MY_INT = 0;

    public static void main(String[] args) {
        new ChangeListener().start();
        new ChangeMaker().start();
    }

    static class ChangeListener extends Thread {

        @Override
        public void run() {
            int local_value = MY_INT;
            while (local_value < 5) {
                if (local_value != MY_INT) {
                    LOGGER.log(Level.INFO, "Got Change for MY_INT : {0}", MY_INT);
                    local_value = MY_INT;
                } 
//                else {
//                    LOGGER.log(Level.INFO, "local_value, MY_INT : {0},{1}", new Object[]{local_value, MY_INT});
//                }
            }
        }
    }

    static class ChangeMaker extends Thread {

        @Override
        public void run() {

            int local_value = MY_INT;
            while (MY_INT < 5) {
                LOGGER.log(Level.INFO, "Incrementing MY_INT to {0}", local_value + 1);
                MY_INT = ++local_value;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
