from __future__ import print_function
from __future__ import division
from __future__ import absolute_import
import unittest

from chaquopy.reflect import jclass


class SimpleEnum(unittest.TestCase):

    def test_enum(self):
        SimpleEnum = jclass('com.chaquo.python.SimpleEnum')
        self.assertTrue(SimpleEnum)

    def test_value(self):
        SimpleEnum = jclass('com.chaquo.python.SimpleEnum')
        self.assertTrue(SimpleEnum.GOOD)
        self.assertTrue(SimpleEnum.BAD)
        self.assertTrue(SimpleEnum.UGLY)
