/*
*The MIT License
*Copyright © 2014-2021 Ilkka Seppälä
*
*Permission is hereby granted, free of charge, to any person obtaining a copy
*of this software and associated documentation files (the "Software"), to deal
*in the Software without restriction, including without limitation the rights
*to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*copies of the Software, and to permit persons to whom the Software is
*furnished to do so, subject to the following conditions:
*
*The above copyright notice and this permission notice shall be included in
*all copies or substantial portions of the Software.
*
*THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
*THE SOFTWARE.
*/

/*
 * The MIT License
 * Copyright © 2014-2021 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.iluwatar.commander;

import com.iluwatar.commander.employeehandle.EmployeeDatabase;
import com.iluwatar.commander.employeehandle.EmployeeHandle;
import com.iluwatar.commander.exceptions.DatabaseUnavailableException;
import com.iluwatar.commander.exceptions.PaymentDetailsErrorException;
import com.iluwatar.commander.messagingservice.MessagingDatabase;
import com.iluwatar.commander.messagingservice.MessagingService;
import com.iluwatar.commander.paymentservice.PaymentDatabase;
import com.iluwatar.commander.paymentservice.PaymentService;
import com.iluwatar.commander.queue.QueueDatabase;
import com.iluwatar.commander.shippingservice.ShippingDatabase;
import com.iluwatar.commander.shippingservice.ShippingService;

/**
 * AppPaymentFailCases class looks at possible cases when Payment service is available/unavailable.
 */

public class AppPaymentFailCases {
  private final int numOfRetries = 3;
  private final long retryDuration = 30000;
  private final long queueTime = 240000; //4 mins
  private final long queueTaskTime = 60000; //1 min
  private final long paymentTime = 120000; //2 mins
  private final long messageTime = 150000; //2.5 mins
  private final long employeeTime = 240000; //4 mins

  void paymentNotPossibleCase() throws Exception {
    var ps = new PaymentService(new PaymentDatabase(), new DatabaseUnavailableException(),
        new PaymentDetailsErrorException());
    var ss = new ShippingService(new ShippingDatabase());
    var ms = new MessagingService(new MessagingDatabase(), new DatabaseUnavailableException());
    var eh = new EmployeeHandle(new EmployeeDatabase());
    var qdb = new QueueDatabase(new DatabaseUnavailableException());
    var c = new Commander(eh, ps, ss, ms, qdb, numOfRetries, retryDuration,
        queueTime, queueTaskTime, paymentTime, messageTime, employeeTime);
    var user = new User("Jim", "ABCD");
    var order = new Order(user, "book", 10f);
    c.placeOrder(order);
  }

  void paymentDatabaseUnavailableCase() throws Exception {
    //rest is successful
    var ps = new PaymentService(new PaymentDatabase(), new DatabaseUnavailableException(),
        new DatabaseUnavailableException(), new DatabaseUnavailableException(),
        new DatabaseUnavailableException(), new DatabaseUnavailableException(),
        new DatabaseUnavailableException());
    var ss = new ShippingService(new ShippingDatabase());
    var ms = new MessagingService(new MessagingDatabase());
    var eh = new EmployeeHandle(new EmployeeDatabase());
    var qdb = new QueueDatabase();
    var c = new Commander(eh, ps, ss, ms, qdb, numOfRetries, retryDuration,
        queueTime, queueTaskTime, paymentTime, messageTime, employeeTime);
    var user = new User("Jim", "ABCD");
    var order = new Order(user, "book", 10f);
    c.placeOrder(order);
  }

  void paymentSuccessCase() throws Exception {
    //goes to message after 2 retries maybe - rest is successful for now
    var ps = new PaymentService(new PaymentDatabase(), new DatabaseUnavailableException(),
        new DatabaseUnavailableException());
    var ss = new ShippingService(new ShippingDatabase());
    var ms =
        new MessagingService(new MessagingDatabase(), new DatabaseUnavailableException());
    var eh = new EmployeeHandle(new EmployeeDatabase());
    var qdb = new QueueDatabase(new DatabaseUnavailableException());
    var c = new Commander(eh, ps, ss, ms, qdb, numOfRetries, retryDuration,
        queueTime, queueTaskTime, paymentTime, messageTime, employeeTime);
    var user = new User("Jim", "ABCD");
    var order = new Order(user, "book", 10f);
    c.placeOrder(order);
  }

  /**
   * Program entry point.
   *
   * @param args command line args
   */

  public static void main(String[] args) throws Exception {
    var apfc = new AppPaymentFailCases();
    //apfc.paymentNotPossibleCase();
    //apfc.paymentDatabaseUnavailableCase();
    apfc.paymentSuccessCase();
  }
}
