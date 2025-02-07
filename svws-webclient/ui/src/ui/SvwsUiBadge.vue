<template>
	<template v-if="short">
		<svws-ui-tooltip :indicator="false">
			<span class="svws-ui-badge badge" :class="{
				'badge--primary': type === 'primary',
				'badge--success': type === 'success',
				'badge--error': type === 'error',
				'badge--highlight': type === 'highlight',
				'badge--light': type === 'light',
				'badge--lg': size === 'big',
				'badge--normal': size === 'normal',
				'badge--small': size === 'small',
				'badge--short': short,
			}">
				<slot />
			</span>
			<template #content>
				<slot />
			</template>
		</svws-ui-tooltip>
	</template>
	<template v-else>
		<span class="svws-ui-badge badge" :class="{
			'badge--primary': type === 'primary',
			'badge--success': type === 'success',
			'badge--error': type === 'error',
			'badge--highlight': type === 'highlight',
			'badge--light': type === 'light',
			'badge--lg': size === 'big',
			'badge--normal': size === 'normal',
			'badge--small': size === 'small',
			'badge--short': short,
		}">
			<slot />
		</span>
	</template>
</template>

<script setup lang='ts'>
	import type { Size, Type } from '../types';

	const props = withDefaults(defineProps<{
		type?: Type;
		size?: Size;
		short?: boolean;
	}>(), {
		type: 'light',
		size: 'normal',
		short: false,
	});
</script>
