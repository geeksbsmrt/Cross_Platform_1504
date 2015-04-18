//
//  SettingsViewController.h
//  testApp
//
//  Created by Adam Crawford on 4/17/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface SettingsViewController : UIViewController
{
	IBOutlet UIButton *logoutButton;
}

-(IBAction)onClick:(id)sender;

@end
