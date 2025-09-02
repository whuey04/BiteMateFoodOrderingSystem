package com.bitemate.controller.user;

import com.bitemate.entity.AddressBook;
import com.bitemate.result.Result;
import com.bitemate.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "User - Address Controller")
public class UserAddressController {

    @Autowired
    private AddressService addressService;

    /**
     * List out all the current user's address
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "Get Address List")
    public Result<List<AddressBook>> getAddressList(){
        return Result.success(addressService.getAddressList());
    }

    /**
     * Add new address
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation(value = "Add New Address")
    public Result addAddress(@RequestBody AddressBook addressBook){
        addressService.addAddress(addressBook);
        return Result.success();
    }

    /**
     * Get address by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get Address By ID")
    public Result<AddressBook> getAddressById(@PathVariable("id") Long id){
        return Result.success(
                addressService.getaddressById(id)
        );
    }

    /**
     * Update address
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation(value = "Update Address")
    public Result updateAddress(@RequestBody AddressBook addressBook){
        addressService.updateAddress(addressBook);
        return Result.success();
    }

    /**
     * Set default address
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation(value = "Set Address as Default")
    public Result setDefaultAddress(@RequestBody AddressBook addressBook){
        addressService.setDefaultAddress(addressBook);
        return Result.success();
    }

    /**
     * Remove address
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "Delete Address")
    public Result deleteAddress(Long id){
        addressService.deleteAddressById(id);
        return Result.success();
    }

    /**
     * Get default address
     * @return
     */
    @GetMapping("default")
    @ApiOperation(value = "Get Default Address")
    public Result<AddressBook> getDefaultAddress(){
        AddressBook addressBook = addressService.getDefaultAddress();
        if (addressBook != null){
            return Result.success(addressBook);
        }
        return Result.error("No default address found !");
    }
}
