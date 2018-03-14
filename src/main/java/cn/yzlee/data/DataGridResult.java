package cn.yzlee.data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:lyz
 * @Date: 2017/12/13 10:38
 * @Desc: 分页信息封装
 **/
public  class DataGridResult implements Serializable
{
    /**
     * 总条数
     */
    private Integer total;

    /**
     * 数据集
     */
    List<?> list;

    //总页码
    private Integer pages;

    //上一页页码
    private Integer prePage;

    //下一页页码
    private Integer nextPage;

    //是否为首页
    private Boolean isFirstPage;

    //是否为末页
    private Boolean isLastPage;

    //是否有上一页
    private Boolean hasPreviousPage;

    //是否有下一页
    private Boolean hasNextPage;


    public DataGridResult(){};

    public static DataGridResult buildDataGridResult(Integer total,List<?> list,Integer currentPage,Integer pageSize){
        DataGridResult gridResult = new DataGridResult();
        Integer pages = (total+pageSize-1)/pageSize;

        gridResult.setPages(pages);
        gridResult.setPrePage(currentPage>1?currentPage-1:1);
        gridResult.setNextPage(currentPage <pages?currentPage+1:pages);
        gridResult.setFirstPage(currentPage==1?true:false);
        gridResult.setLastPage(currentPage == pages?true:false);
        gridResult.setHasPreviousPage(currentPage>1?true:false);
        gridResult.setHasNextPage(pages>currentPage?true:false);
        gridResult.setList(list);
        return  gridResult;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public void setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public static void main(String[] args){
        Integer total = 90;
        Integer currentPage = 1;
        Integer pageSize = 15;
        Integer pages = (total+pageSize-1)/pageSize;
        System.out.println("总条数---"+total);
        System.out.println("当前页---"+currentPage);
        System.out.println("总页数---"+pages);
        System.out.println("总页数---"+pages);
        System.out.println("总页数---"+pages);
    }
}
