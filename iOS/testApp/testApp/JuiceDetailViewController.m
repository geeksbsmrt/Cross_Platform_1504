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
	switch ([sender tag]) {
		case 0: {
			//save
			NSError *error = NULL;
			NSString *input = flavorField.text;
			NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:@"^\\s|\\s+$" options:NSRegularExpressionCaseInsensitive error:&error];
			if (error) {
				NSLog(@"Error in regex");
			}
			NSUInteger countMatches = [regex numberOfMatchesInString:input options:0 range:NSMakeRange(0, [input length])];
			NSLog(@"%d", (int)countMatches);
			if ([input length] == 0 || countMatches > 0) {
				UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Invalid Juice Name"
																message:@"Name must be entered and not start or end with space"
															   delegate:self
													  cancelButtonTitle:@"OK"
													  otherButtonTitles:nil];
				[alert show];
			} else {
				PFObject *juice = NULL;
				BOOL connected = [(AppDelegate *) [[UIApplication sharedApplication] delegate] connected];
				if (sending) {
					sending[@"Flavor"] = flavorField.text;
					sending[@"Rating"] = [NSNumber numberWithDouble:ratingStepper.value];
					juice = sending;
					
				} else {
					juice = [PFObject objectWithClassName:@"Juice"];
					juice[@"Flavor"] = flavorField.text;
					juice[@"Rating"] = [NSNumber numberWithDouble:ratingStepper.value];
					juice.ACL = [PFACL ACLWithUser:[PFUser currentUser]];
				}
				if (connected) {
					[juice saveInBackground];
				} else {
					[juice pinInBackground];
				}
				[self dismissViewControllerAnimated:TRUE completion:nil];
				self.tabBarController.selectedIndex = 0;
			}
			break;
		}
		case 2: {
			//cancel
			[self dismissViewControllerAnimated:TRUE completion:nil];
			self.tabBarController.selectedIndex = 0;
			break;
		}
		default:
			break;
	}
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
	[alertView dismissWithClickedButtonIndex:buttonIndex animated:YES];
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
