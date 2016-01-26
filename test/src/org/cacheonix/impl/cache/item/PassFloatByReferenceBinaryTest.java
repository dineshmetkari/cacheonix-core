/*
 * Cacheonix systems licenses this file to You under the LGPL 2.1
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
package org.cacheonix.impl.cache.item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import org.cacheonix.impl.net.serializer.Serializer;
import org.cacheonix.impl.net.serializer.SerializerFactory;
import org.cacheonix.impl.net.serializer.SerializerUtils;
import org.cacheonix.impl.util.logging.Logger;

/**
 * Tester for  PassFloatByReferenceBinary.
 */
public final class PassFloatByReferenceBinaryTest extends TestCase {

   /**
    * Logger.
    *
    * @noinspection UNUSED_SYMBOL, UnusedDeclaration
    */
   private static final Logger LOG = Logger.getLogger(PassFloatByReferenceBinaryTest.class); // NOPMD

   private static final Float TEST_VALUE = 1234.56f;

   private PassFloatByReferenceBinary binary;


   public void testSetValue() {

      assertEquals(TEST_VALUE, binary.getValue());
   }


   public void testSerializeDeserialize() throws IOException {

      final Serializer ser = SerializerFactory.getInstance().getSerializer(Serializer.TYPE_JAVA);
      assertEquals(binary, ser.deserialize(ser.serialize(binary)));
   }


   public void testSerializeDeserializeUsingUtils() throws IOException, ClassNotFoundException {

      // Write
      final ByteArrayOutputStream baos = new ByteArrayOutputStream(111);
      final DataOutputStream dos = new DataOutputStream(baos);
      SerializerUtils.writeBinary(dos, binary);
      dos.flush();

      // Read
      final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      final DataInputStream dis = new DataInputStream(bais);
      final Binary newBinary = SerializerUtils.readBinary(dis);
      assertEquals(binary, newBinary);
   }


   public void testReadWriteExternal() throws IOException, ClassNotFoundException {

      final ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
      final ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(binary);
      oos.flush();

      assertEquals(binary, new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject());
   }


   public void testEquals() {

      assertEquals(new PassFloatByReferenceBinary(TEST_VALUE), new PassFloatByReferenceBinary(TEST_VALUE));
   }


   protected final void setUp() throws Exception {

      super.setUp();
      binary = new PassFloatByReferenceBinary(TEST_VALUE);
   }


   public String toString() {

      return "PassFloatByReferenceBinaryTest{" +
              "binary=" + binary +
              "} " + super.toString();
   }
}