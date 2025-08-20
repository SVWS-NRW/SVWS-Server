<template>
	<template v-if="!headless">
		<div v-if="mode === 'radio'" class="flex flex-col gap-2 text-left">
			<span class="font-bold">Theme</span>
			<svws-ui-radio-group :row="true">
				<svws-ui-radio-option value="light" v-model="colormode" name="colormode" label="Light" />
				<svws-ui-radio-option value="dark" v-model="colormode" name="colormode" label="Dark" />
				<svws-ui-radio-option value="auto" v-model="colormode" name="colormode" label="System/Browsereinstellungen" />
			</svws-ui-radio-group>
			<svws-ui-notification v-if="colormode === 'dark' && warningDark" type="warning">
				Achtung! Das Dark-Theme befindet sich gerade noch in der Entwicklung und ist noch nicht
				vollständig umgesetzt.
				<br>Es kann an einigen Stellen zu Darstellungsproblemen führen.
			</svws-ui-notification>
		</div>
		<div v-else>
			<svws-ui-button type="icon" @click="next" title="Umschalten zwischen Dark-Mode und Light-Mode">
				<span v-if="colormode === 'dark'" class="icon i-ri-moon-line" />
				<span v-if="colormode === 'light'" class="icon i-ri-sun-line" />
			</svws-ui-button>
		</div>
	</template>
</template>

<script setup lang="ts">

	import { useColorMode } from '@vueuse/core';

	const colormode = useColorMode();

	const props = withDefaults(defineProps<{
		mode?: 'radio' | 'icons';
		auto?: boolean;
		warningDark?: boolean;
		headless?: boolean;
	}>(), {
		mode: 'icons',
		auto: false,
		warningDark: false,
		headless: false,
	});

	function next() {
		switch (colormode.value) {
			case 'dark':
				colormode.value = 'light';
				break;
			case 'light':
				colormode.value = 'dark';
				break;
			default:
				break;
		}
	}

</script>