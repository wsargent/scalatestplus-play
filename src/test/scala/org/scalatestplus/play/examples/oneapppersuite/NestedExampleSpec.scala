/*
 * Copyright 2001-2014 Artima, Inc.
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
package org.scalatestplus.play.examples.oneapppersuite

import play.api.test._
import org.scalatest._
import org.scalatestplus.play._
import play.api.{Play, Application}

// This is the "master" suite
class NestedExampleSpec extends Suites(
  new OneSpec,
  new TwoSpec,
  new RedSpec,
  new BlueSpec
) with OneAppPerSuite {
  // Override app if you need a FakeApplication with other than non-default parameters.
  implicit override lazy val app: FakeApplication =
    FakeApplication(additionalConfiguration = Map("ehcacheplugin" -> "disabled"))
}

// These are the nested suites
@DoNotDiscover class OneSpec extends PlaySpec with ConfiguredApp
@DoNotDiscover class TwoSpec extends PlaySpec with ConfiguredApp
@DoNotDiscover class RedSpec extends PlaySpec with ConfiguredApp

@DoNotDiscover
class BlueSpec extends PlaySpec with ConfiguredApp {

  "The OneAppPerSuite trait" must {
    "provide a FakeApplication" in { 
      app.configuration.getString("ehcacheplugin") mustBe Some("disabled")
    }
    "make the FakeApplication available implicitly" in {
      def getConfig(key: String)(implicit app: Application) = app.configuration.getString(key)
      getConfig("ehcacheplugin") mustBe Some("disabled")
    }
    "start the FakeApplication" in {
      Play.maybeApplication mustBe Some(app)
    }
  }
}


