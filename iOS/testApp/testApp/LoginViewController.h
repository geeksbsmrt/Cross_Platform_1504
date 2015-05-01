//
//  ViewController.h
//  testApp
//
//  Created by Adam Crawford on 4/16/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ParseUI/ParseUI.h>
#import <Parse/Parse.h>
#import "JuiceListViewController.h"
#import "AppDelegate.h"

@interface LoginViewController : UIViewController <PFLogInViewControllerDelegate, PFSignUpViewControllerDelegate, UIAlertViewDelegate>
{
	PFLogInViewController *logInViewController;
}


@end

