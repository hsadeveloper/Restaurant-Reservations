package tableservice.adapter.out;

import java.util.List;
import org.springframework.stereotype.Component;
import tableservice.domain.TableDefination;
import tableservice.port.out.TablePersistencePort;

@Component
public class ManageTableAdapter implements TablePersistencePort {

  @Override
  public List<TableDefination> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TableDefination getByTableId(String tableId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TableDefination getBySize(int size) {
    // TODO Auto-generated method stub
    return null;
  }



}
