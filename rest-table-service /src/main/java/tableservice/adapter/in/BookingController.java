package tableservice.adapter.in;


import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tableservice.adapter.out.ManageTableAdapter;
import tableservice.domain.TableDefination;



@RestController
@RequestMapping("/api/tables")
public class BookingController {

  private final ManageTableAdapter tableAdapter;

  public BookingController(ManageTableAdapter tableJpaAdapter) {
    super();
    this.tableAdapter = tableJpaAdapter;
  }

  @GetMapping("/all")
  public List<TableDefination> getAllReservations() {
    return tableAdapter.findAll();
  }

  @GetMapping("/size")
  public ResponseEntity<TableDefination> getTable(@RequestParam("id") int tableSize) {

    TableDefination result = tableAdapter.getBySize(tableSize);
    if (result == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(result);
  }



}
