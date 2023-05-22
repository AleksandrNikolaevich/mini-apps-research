import React from 'react';
import {Button, Platform, StyleSheet, View} from 'react-native';

import {NavigationContainer, useNavigation} from '@react-navigation/native';

import {createStackNavigator} from '@react-navigation/stack';
import {MiniApp} from './mini-app';

const Stack = createStackNavigator();

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="home" component={Home} />
        <Stack.Screen name="miniApp" component={MiniAppScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

const Home = () => {
  const {navigate} = useNavigation<any>();
  return (
    <View style={styles.root}>
      <Button title="open mini app" onPress={() => navigate('miniApp')} />
    </View>
  );
};

const MiniAppScreen = () => {
  // const ref = useRef<MiniAppRef>(null);
  // useEffect(() => {
  //   const timer = setTimeout(() => {
  //     ref.current?.dispatch();
  //   }, 2000);
  //   return () => {
  //     clearTimeout(timer);
  //   };
  // }, []);
  return (
    <MiniApp
      // ref={ref}
      source={{
        moduleName: 'MiniApp',
        /**
         * example for clean bundle
         */
        url: `https://github.com/AleksandrNikolaevich/shared/raw/main/miniapps/miniapp.${Platform.OS}.bundle`,
        /**
         * example for bundle with resources such as images, videos, etc.
         */
        // url: `https://github.com/AleksandrNikolaevich/shared/raw/main/miniapps/bundle.${Platform.OS}.zip`,
      }}
    />
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default App;
