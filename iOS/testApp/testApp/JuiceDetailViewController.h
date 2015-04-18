//
//  JuiceDetailViewController.h
//  testApp
//
//  Created by Adam Crawford on 4/17/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface JuiceDetailViewController : UIViewController
{
	IBOutlet UITextField *flavorField;
	IBOutlet UIStepper *ratingStepper;
	IBOutlet UILabel *ratingLabel;
	IBOutlet UIButton *saveButton;
	IBOutlet UIButton *cancelButton;
}

@property (strong, nonatomic) PFObject *sending;

-(IBAction)onClick:(id)sender;

@end
