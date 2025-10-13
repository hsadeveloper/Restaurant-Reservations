package orderservice.entity;

@FeignClient(name = "table-service", url = "${table.service.url}")
public interface TableServiceClient {
    @GetMapping("/api/tables/{id}")
    TableDTO getTableById(@PathVariable Long id);
}