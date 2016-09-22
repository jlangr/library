package controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import api.library.BranchService;

@RestController
public class BranchController {
   private BranchService service = new BranchService();

   @RequestMapping(value="/branches", method= { RequestMethod.POST })
   public String add(@RequestBody BranchRequest branchRequest) {
      // TODO duplicate/error
      return service.add(branchRequest.getName());
   }

   @RequestMapping(value="/branches", method = { GET })
   public List<BranchRequest> retrieveAll() {
       return service.allBranches().stream()
          .map(branch -> new BranchRequest(branch))
          .collect(Collectors.toList());
   }
}
