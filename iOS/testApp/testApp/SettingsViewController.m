//
//  SettingsViewController.m
//  testApp
//
//  Created by Adam Crawford on 4/17/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import "SettingsViewController.h"

@interface SettingsViewController ()

@end

@implementation SettingsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onClick:(id)sender {
	[PFUser logOut];
	UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
	UIViewController *vc = [sb instantiateInitialViewController];
	[self presentViewController:vc animated:YES completion:NULL];
}
@end
