import React, {
  forwardRef,
  useCallback,
  useImperativeHandle,
  useRef,
  useState,
} from 'react';
import {
  ActivityIndicator,
  Alert,
  NativeSyntheticEvent,
  StyleProp,
  StyleSheet,
  UIManager,
  View,
  ViewStyle,
  findNodeHandle,
  requireNativeComponent,
  Platform,
} from 'react-native';

interface MiniAppProps {
  style?: StyleProp<ViewStyle>;
  source: {
    /**
     * url bundle (.bundle or .zip)
     */
    url: string;
    /**
     * name of module for run
     */
    moduleName: string;
  };
  onStartLoad: () => void;
  onEndLoad: () => void;
  onError: (e: NativeSyntheticEvent<{code: string; message: string}>) => void;
}

export interface MiniAppRef {
  dispatch: () => void;
}

type TMiniAppProps = Omit<
  MiniAppProps,
  'onStartLoad' | 'onEndLoad' | 'onError'
>;

export const Component = requireNativeComponent<MiniAppProps>('MiniApp');

const getCommand = (name: string) => {
  const command = UIManager.getViewManagerConfig('MiniApp').Commands[name];
  return Platform.OS === 'android' ? command.toString() : command;
};

const dispatch = (viewId: number) => {
  UIManager.dispatchViewManagerCommand(viewId, getCommand('dispatch'), []);
};

export const MiniApp = forwardRef<MiniAppRef, TMiniAppProps>(
  ({style, ...props}, ref) => {
    const nativeRef = useRef(null);
    const [isLoading, setIsLoading] = useState(false);
    const onStartLoad = useCallback(() => {
      setIsLoading(true);
    }, []);
    const onEndLoad = useCallback(() => {
      setIsLoading(false);
    }, []);

    useImperativeHandle(
      ref,
      () => ({
        dispatch: () => {
          let viewId = findNodeHandle(nativeRef.current);
          if (viewId != null) {
            dispatch(viewId);
          }
        },
      }),
      [],
    );

    return (
      <View style={[styles.root, style]}>
        <Component
          {...props}
          ref={nativeRef}
          style={isLoading ? {flex: 0} : styles.root}
          onStartLoad={onStartLoad}
          onEndLoad={onEndLoad}
          onError={({nativeEvent}) => {
            Alert.alert('Error', nativeEvent.message);
          }}
        />
        {isLoading && (
          <View style={styles.loader}>
            <ActivityIndicator />
          </View>
        )}
      </View>
    );
  },
);

const styles = StyleSheet.create({
  root: {flex: 1},
  loader: {
    ...StyleSheet.absoluteFillObject,
    zIndex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
