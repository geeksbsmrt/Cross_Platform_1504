//
//  AppDelegate.h
//  testApp
//
//  Created by Adam Crawford on 4/16/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import "Reachability.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (retain, nonatomic) Reachability *reachable;
@property (nonatomic, assign) BOOL connected;

-(void)connectionChanged:(NSNotification *)notice;
-(void)syncParse;

@end

