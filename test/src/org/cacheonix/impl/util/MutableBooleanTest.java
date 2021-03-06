/*
 * Cacheonix Systems licenses this file to You under the LGPL 2.1
 * (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.cacheonix.org/products/cacheonix/license-lgpl-2.1.htm
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cacheonix.impl.util;

import org.cacheonix.CacheonixTestCase;

/**
 * Tester for {@link MutableBoolean}.
 */
public final class MutableBooleanTest extends CacheonixTestCase {

   private MutableBoolean mutableBoolean;


   public void testGet() throws Exception {

      assertFalse("Default initial value should be false", mutableBoolean.get());
   }


   public void testSet() throws Exception {

      mutableBoolean.set(true);
      assertTrue(mutableBoolean.get());
   }


   public void testCreate() {

      assertTrue(new MutableBoolean(true).get());
   }


   public void testToString() {

      assertNotNull(mutableBoolean.toString());
   }


   protected void setUp() throws Exception {

      super.setUp();

      mutableBoolean = new MutableBoolean();
   }


   public String toString() {

      return "MutableBooleanTest{" +
              "mutableBoolean=" + mutableBoolean +
              "} " + super.toString();
   }
}
