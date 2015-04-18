//
//  JuiceListViewController.h
//  testApp
//
//  Created by Adam Crawford on 4/17/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import <ParseUI/ParseUI.h>
#import <ParseUI/PFQueryTableViewController.h>
#import "JuiceCell.h"
#import "JuiceDetailViewController.h"

@interface JuiceListViewController : PFQueryTableViewController
{
	IBOutlet UITableView *table;
}


@end
