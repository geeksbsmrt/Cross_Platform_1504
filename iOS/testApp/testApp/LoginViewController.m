//
//  ViewController.m
//  testApp
//
//  Created by Adam Crawford on 4/16/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import "LoginViewController.h"

@interface LoginViewController ()

@end

@implementation LoginViewController

- (void)viewDidLoad {
	[super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
	// Dispose of any resources that can be recreated.
}

- (void) viewDidAppear:(BOOL)animated
{
	BOOL connected = [(AppDelegate *) [[UIApplication sharedApplication] delegate] connected];
	if (connected) {
		if (![PFUser currentUser]) { // No user logged in
									 // Create the log in view controller
			logInViewController = [[PFLogInViewController alloc] init];
			[logInViewController setDelegate:self]; // Set ourselves as the delegate
			
			// Create the sign up view controller
			PFSignUpViewController *signUpViewController = [[PFSignUpViewController alloc] init];
			[signUpViewController setDelegate:self]; // Set ourselves as the delegate
			
			// Assign our sign up controller to be displayed from the login controller
			[logInViewController setSignUpController:signUpViewController];
			
			// Present the log in view controller
			[self presentViewController:logInViewController animated:YES completion:NULL];
		} else {
			[self performSegueWithIdentifier: @"listSegue" sender: self];
		}
	} else {
		if([PFUser currentUser]){
			[self performSegueWithIdentifier: @"listSegue" sender: self];
		} else {
			//Not Connected and no current user
			UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No Network Connection"
															message:@"You must have a valid network connection to login or signup."
														   delegate:self
												  cancelButtonTitle:@"OK"
												  otherButtonTitles:nil];
			[alert show];
		}
	}
	
}
#pragma mark -
#pragma mark PFLogInViewControllerDelegate

- (void)logInViewController:(PFLogInViewController *)logInController didLogInUser:(PFUser *)user {
	[self performSegueWithIdentifier: @"listSegue" sender: self];
	[logInController dismissViewControllerAnimated:YES completion:NULL];
}

- (void)logInViewControllerDidCancelLogIn:(PFLogInViewController *)logInController {
	// Do nothing, as the view controller dismisses itself
}

#pragma mark -
#pragma mark PFSignUpViewControllerDelegate

- (void)signUpViewController:(PFSignUpViewController *)signUpController didSignUpUser:(PFUser *)user {
	[self performSegueWithIdentifier: @"listSegue" sender: self];
	[signUpController dismissViewControllerAnimated:YES completion:NULL];
	[logInViewController dismissViewControllerAnimated:YES completion:NULL];
}

- (void)signUpViewControllerDidCancelSignUp:(PFSignUpViewController *)signUpController {
	// Do nothing, as the view controller dismisses itself
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
	[alertView dismissWithClickedButtonIndex:buttonIndex animated:YES];
}


@end
