package controller;

import org.springframework.web.bind.annotation.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import api.library.HoldingService;

@RestController
public class HoldingController {
   private HoldingService service = new HoldingService();

   @RequestMapping(value = "/holdings", method = { POST })
   public void addHolding(@RequestBody AddHoldingRequest request) {
      // TODO need material type also
      // Does API format change?
      service.add(request.getSourceId(), request.getBranchId());
   }

   @RequestMapping(value = "/holdings/{holdingBarcode}", method = { GET })
   public HoldingResponse retrieve(@PathVariable("holdingBarcode") String holdingBarcode) {
      System.out.println("barcode:" + holdingBarcode);
      return new HoldingResponse(service.find(holdingBarcode));
   }
}