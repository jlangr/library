package controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import api.library.BranchService;

// TODO common route portions
@RestController
public class BranchController {
   private BranchService service = new BranchService();

   @RequestMapping(value="/branches", method= { POST })
   public String add(@RequestBody BranchRequest branchRequest) {
      // TODO on duplicate/error?
      return service.add(branchRequest.getName());
   }

   @RequestMapping(value="/branches", method = { GET })
   public List<BranchRequest> retrieveAll() {
       return service.allBranches().stream()
          .map(branch -> new BranchRequest(branch))
          .collect(Collectors.toList());
   }
}
