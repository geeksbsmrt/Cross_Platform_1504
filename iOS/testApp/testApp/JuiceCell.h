//
//  JuiceCell.h
//  testApp
//
//  Created by Adam Crawford on 4/17/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import <ParseUI/ParseUI.h>
#import <UIKit/UIKit.h>
#import <ParseUI/PFTableViewCell.h>

@interface JuiceCell : PFTableViewCell
{
	
}
@property (strong, nonatomic) IBOutlet UILabel *juiceName;
@property (strong, nonatomic) IBOutlet UILabel *juiceRating;

@end
