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
package org.cacheonix.impl.cache.item;

import org.cacheonix.cache.Immutable;

/**
 * Decorator.
 */
final class ImmutableBinaryFactoryDecorator implements BinaryFactory {

   private final ImmutableBinaryFactory immutableBinaryFactory = new ImmutableBinaryFactory();

   private BinaryFactory binaryFactory = null;


   /**
    * @param binaryFactory item factory.
    * @noinspection PublicConstructorInNonPublicClass
    */
   public ImmutableBinaryFactoryDecorator(final BinaryFactory binaryFactory) {

      this.binaryFactory = binaryFactory;
   }


   public Binary createBinary(final Object object) throws InvalidObjectException {

      if (isImmutable(object)) {
         return immutableBinaryFactory.createBinary(object);
      } else {
         return binaryFactory.createBinary(object);
      }
   }


   private static boolean isImmutable(final Object object) {

      return object instanceof String
              || object instanceof Number
              || object instanceof Boolean
              || object instanceof Immutable
              ;
   }


   public String toString() {

      return "ImmutableItemFactoryDecorator{" +
              "itemFactory=" + binaryFactory +
              '}';
   }
}
