/**
 * To do list file
 * 
 */

/*
 * 
- add field of "implementation loss" in receiver. and add it to calculation of sensitivity when choosing SNR instead of sensitivity
- replace TextArea with TextPane, to be able paint some text in colors

- calculate the fade margin as a function of reliability in percentage

- add tooltip help:
  Log-distance Path Loss Model: calculate the path loss by relation to a reference point, d0 (from the range test we did): 
  pr = pt+PL(d0)-10*Gama*log10(d/d0)
  or
  L50 = PL(d0) + 10*Gama*log10(d/d0) + Xs
  
  where: 
        Gama - path loss exponent and can be between 2 to 5.
		Xs - flat fading attenuation constant 
  PL(d0) is a unitless constant that depends on the antenna characteristics and free-space path loss up to distance d0.


 */

package com.mylib.rf.linkbudget;

