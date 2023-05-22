import React from 'react';
import {
  Alert,
  Button,
  SafeAreaView,
  StyleSheet,
  Text,
  View,
  Image,
} from 'react-native';
import {Images} from './assets';

function App(): JSX.Element {
  const onPress = () => {
    Alert.alert('Hi everybody!');
  };

  return (
    <SafeAreaView style={styles.root}>
      <Text>Mini app</Text>
      <Button title="Close mini app" onPress={onPress} />
      <Image source={Images.logo} style={styles.img} />
      <View style={[styles.edge, styles.leftTop]} />
      <View style={[styles.edge, styles.rightTop]} />
      <View style={[styles.edge, styles.leftBottom]} />
      <View style={[styles.edge, styles.rightBottom]} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  img: {
    width: 100,
    height: 100,
  },
  root: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center',
    justifyContent: 'center',
  },
  edge: {
    position: 'absolute',
    width: 100,
    height: 100,
    backgroundColor: 'red',
  },
  leftTop: {
    left: 0,
    top: 0,
  },
  rightTop: {
    right: 0,
    top: 0,
  },
  leftBottom: {
    left: 0,
    bottom: 0,
  },
  rightBottom: {
    bottom: 0,
    right: 0,
  },
});

export default App;
