package com.youlai.mall.pms.controller.app;

import cn.hutool.core.bean.BeanUtil;
import com.youlai.common.result.Result;
import com.youlai.mall.pms.pojo.domain.PmsInventory;
import com.youlai.mall.pms.pojo.dto.SkuDTO;
import com.youlai.mall.pms.pojo.vo.SkuInfoVO;
import com.youlai.mall.pms.pojo.vo.WareSkuStockVO;
import com.youlai.mall.pms.service.IPmsInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "【移动端】商品库存")
@RestController("AppSkuController")
@RequestMapping("/api.app/v1/sku")
@AllArgsConstructor
public class SkuController {

    private IPmsInventoryService iPmsInventoryService;


    @ApiOperation(value = "商品sku详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "商品sku id", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{id}")
    public Result<SkuDTO> detail(@PathVariable Long id) {
        PmsInventory sku = iPmsInventoryService.getById(id);
        SkuDTO skuDTO = new SkuDTO();
        BeanUtil.copyProperties(sku, skuDTO);
        return Result.success(skuDTO);
    }


    @ApiOperation(value = "修改库存", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Sku ID", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "num", value = "库存数量", required = true, paramType = "query", dataType = "Long")
    })
    @PutMapping("/{id}/stock")
    public Result updateStock(@PathVariable Long id, @RequestParam Integer num) {
        PmsInventory sku = iPmsInventoryService.getById(id);
        sku.setInventory(sku.getInventory() + num);
        boolean result = iPmsInventoryService.updateById(sku);
        return Result.judge(result);
    }

    @ApiOperation(value = "批量获取商品详情", httpMethod = "GET")
    @ApiImplicitParam(name = "skuIds", value = "Sku ID 集合", required = true, paramType = "param", dataType = "List")
    @GetMapping("/infos")
    public Result<List<SkuInfoVO>> infos(@RequestParam("skuId") List<String> skuIds) {
        List<SkuInfoVO> infos = iPmsInventoryService.getSkuInfoByIds(skuIds);
        return Result.success(infos);
    }


    @ApiOperation(value = "订单下单锁定库存", httpMethod = "POST")
    @ApiImplicitParam(name = "skuStockVO", value = "订单库存信息", required = true, paramType = "body", dataType = "WareSkuStockVO")
    @PostMapping("/stock/lock")
    public Result<Boolean> lockStock(@RequestBody WareSkuStockVO skuStockVO) {

        try {
            iPmsInventoryService.lockStock(skuStockVO);
            return Result.success();
        } catch (Exception e) {
            return Result.failed();
        }
    }

    @ApiOperation(value = "订单下单释放库存", httpMethod = "POST")
    @ApiImplicitParam(name = "skuStockVO", value = "订单库存信息", required = true, paramType = "body", dataType = "WareSkuStockVO")
    @PostMapping("/stock/release")
    public Result<Boolean> releaseStock(@RequestBody WareSkuStockVO skuStockVO) {

        try {
            iPmsInventoryService.releaseStock(skuStockVO);
            return Result.success();
        } catch (Exception e) {
            return Result.failed();
        }
    }

}
