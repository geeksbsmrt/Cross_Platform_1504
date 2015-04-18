//
//  JuiceDetailViewController.m
//  testApp
//
//  Created by Adam Crawford on 4/17/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import "JuiceDetailViewController.h"

@interface JuiceDetailViewController ()

@end

@implementation JuiceDetailViewController

@synthesize sending;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
	
	if (sending){
		flavorField.text = sending[@"Flavor"];
		ratingLabel.text = [NSString stringWithFormat:@"%@", sending[@"Rating"]];
		ratingStepper.value = [sending[@"Rating"] doubleValue];
		cancelButton.hidden = NO;
	}
	
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)valueChanged:(UIStepper *)sender {
	double value = [sender value];
	
	[ratingLabel setText:[NSString stringWithFormat:@"%d", (int)value]];
}

- (IBAction)onClick:(id)sender{
	UIButton *pressed = (UIButton*)sender;
	switch (pressed.tag) {
		case 0:
			//save
		{
		if (sending) {
			sending[@"Flavor"] = flavorField.text;
			sending[@"Rating"] = [NSNumber numberWithDouble:ratingStepper.value];
			[sending saveInBackground];
			[self dismissViewControllerAnimated:TRUE completion:nil];
		} else {
			PFObject *juice = [PFObject objectWithClassName:@"Juice"];
			juice[@"Flavor"] = flavorField.text;
			juice[@"Rating"] = [NSNumber numberWithDouble:ratingStepper.value];
			juice.ACL = [PFACL ACLWithUser:[PFUser currentUser]];
			[juice saveInBackground];
			UITabBarController *tabBarController = self.tabBarController;
			UIView * fromView = tabBarController.selectedViewController.view;
			UIView * toView = [[tabBarController.viewControllers objectAtIndex:0] view];
			
			[UIView transitionFromView:fromView
								toView:toView
							  duration:0.5
							   options:UIViewAnimationOptionBeginFromCurrentState
							completion:^(BOOL finished) {
								if (finished) {
									tabBarController.selectedIndex = 0;
								}
							}];
		}
		break;
		}
		case 1:
			//Cancel
		{
		[self dismissViewControllerAnimated:TRUE completion:nil];
		break;
		}
		default:
			break;
	}
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
