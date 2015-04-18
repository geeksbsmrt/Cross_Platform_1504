//
//  JuiceCell.m
//  testApp
//
//  Created by Adam Crawford on 4/17/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import "JuiceCell.h"

@implementation JuiceCell

@synthesize juiceRating;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
	self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
	if (self) {
		// Initialization code
	}
	return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
	[super setSelected:selected animated:animated];
	
	// Configure the view for the selected state
}

@end
