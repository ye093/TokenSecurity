package cn.yejy.data;

//传参数格式 page=1&size=20&sort=card_no,desc
public class Page {
    private Boolean first; // 是否是第一条
    private Boolean last; // 是否是最后一条
    private Integer number; // 当前页数
    private Integer size; // 分页大小
    private Integer numberOfElements; // 返回条数
    private Integer totalElements; // 总共条数
    private Integer totalPages; // 总页数
    private Boolean sorted; // 是否排序了
    private Object content; //内容

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getSorted() {
        return sorted;
    }

    public void setSorted(Boolean sorted) {
        this.sorted = sorted;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Page{" +
                "first=" + first +
                ", last=" + last +
                ", number=" + number +
                ", size=" + size +
                ", numberOfElements=" + numberOfElements +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", sorted=" + sorted +
                ", content=" + content +
                '}';
    }
}
