package controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HoldingController {
   @RequestMapping(value = "/holdings", method = { RequestMethod.POST })
   public int addHolding(@RequestBody AddHoldingRequest request) {
      System.out.println("request:" + request.getHoldingBarcode());
      return 1;
   }
}