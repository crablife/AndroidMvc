/*
 * Copyright 2016 Kejun Xia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shipdream.lib.android.mvp;

import org.junit.Assert;
import org.junit.Test;

public class TestMvpBean {
    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_bind_null_to_a_mvpBean() {
        MvpBean mvpBean = new MvpBean() {
            @Override
            public Class modelType() {
                return String.class;
            }
        };

        mvpBean.bindModel(null);
    }

    @Test
    public void should_rebind_state_after_restoring_mvpBean() {
        MvpBean<String> mvpBean = new MvpBean() {

            @Override
            public Class modelType() {
                return String.class;
            }
        };

        Assert.assertNull(mvpBean.getModel());

        mvpBean.restoreModel("A");

        Assert.assertEquals("A", mvpBean.getModel());
    }

    @Test
    public void should_call_on_restore_call_back_after_a_stateful_mvpBean_is_restored() {
        class MyMvpBean extends MvpBean<String> {
            private boolean called = false;

            @Override
            public Class modelType() {
                return String.class;
            }

            @Override
            public void onRestored() {
                super.onRestored();
                called = true;
            }
        };

        MyMvpBean mvpBean = new MyMvpBean();

        Assert.assertFalse(mvpBean.called);

        mvpBean.restoreModel("A");

        Assert.assertTrue(mvpBean.called);
    }

    @Test
    public void should_not_call_on_restore_call_back_after_a_non_stateful_mvpBean_is_restored() {
        class MyMvpBean extends MvpBean<String> {
            private boolean called = false;

            @Override
            public Class modelType() {
                return null;
            }

            @Override
            public void onRestored() {
                super.onRestored();
                called = true;
            }
        };

        MyMvpBean mvpBean = new MyMvpBean();

        Assert.assertFalse(mvpBean.called);

        mvpBean.restoreModel("A");

        Assert.assertFalse(mvpBean.called);
    }

    public void should_create_state_instance_on_construct_when_the_state_type_is_specified_for_a_mvpBean() {
        class MyMvpBean extends MvpBean<String> {
            @Override
            public Class modelType() {
                return String.class;
            }
        };
        MyMvpBean mvpBean = new MyMvpBean();

        Assert.assertNull(mvpBean.getModel());

        mvpBean.onConstruct();

        Assert.assertNotNull(mvpBean.getModel());
    }

    public void should_NOT_create_state_instance_on_construct_when_the_state_type_is_null_for_a_mvpBean() {
        class MyMvpBean extends MvpBean {
            @Override
            public Class modelType() {
                return null;
            }
        };
        MyMvpBean mvpBean = new MyMvpBean();

        Assert.assertNull(mvpBean.getModel());

        mvpBean.onConstruct();

        Assert.assertNull(mvpBean.getModel());
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception_out_when_creating_state_failed() {
        class BadClass {
            {int x = 1 / 0;}
        }

        class MyMvpBean extends MvpBean<BadClass> {
            @Override
            public Class<BadClass> modelType() {
                return BadClass.class;
            }
        };

        MyMvpBean mvpBean = new MyMvpBean();

        mvpBean.onConstruct();
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_binding_null_to_stateful_mvpBean() {
        class MyMvpBean extends MvpBean<String> {
            @Override
            public Class<String> modelType() {
                return String.class;
            }
        };

        MyMvpBean mvpBean = new MyMvpBean();

        mvpBean.bindModel(null);
    }

    @Test
    public void should_be_able_to_successfully_bind_state_to_stateful_mvpBean() {
        class MyMvpBean extends MvpBean<String> {
            @Override
            public Class<String> modelType() {
                return String.class;
            }
        };

        MyMvpBean mvpBean = new MyMvpBean();

        Assert.assertNotEquals("B", mvpBean.getModel());

        mvpBean.bindModel("B");

        Assert.assertEquals("B", mvpBean.getModel());
    }

}