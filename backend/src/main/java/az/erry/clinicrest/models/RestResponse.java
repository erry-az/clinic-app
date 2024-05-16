package az.erry.clinicrest.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestResponse<T> {
    private T data;
    private String errors;
    private PagingResponse paging;
}
