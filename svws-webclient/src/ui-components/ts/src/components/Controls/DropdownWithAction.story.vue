<script setup lang="ts">
	import { hstEvent } from 'histoire/client';

	import SvwsUiDropdownWithAction from "./DropdownWithAction.vue";
	import SvwsUiDropdownItem from "./DropdownItem.vue";
	import SvwsUiButton from "./Button.vue";

	function onClick(event: Event) {
		hstEvent('click', event);
	}
</script>

<template>
	<Story title="SVWS UI/Controls/Dropdown with action">
		<Variant title="Primary">
			<svws-ui-dropdown-with-action type="primary">
				<template #actionButton>
					<svws-ui-button dropdown-action @click="onClick">Button und Dropdown</svws-ui-button>
				</template>
				<template #dropdownItems>
					<svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
					<svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
					<svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
					<svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
				</template>
			</svws-ui-dropdown-with-action>
		</Variant>

		<Variant title="Secondary">
			<svws-ui-dropdown-with-action type="secondary">
				<template #actionButton>
					<svws-ui-button dropdown-action @click="onClick">Button und Dropdown</svws-ui-button>
				</template>
				<template #dropdownItems>
					<svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
					<svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
					<svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
					<svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
				</template>
			</svws-ui-dropdown-with-action>
		</Variant>

		<Variant title="Error">
			<svws-ui-dropdown-with-action type="error">
				<template #actionButton>
					<svws-ui-button dropdown-action @click="onClick">Button und Dropdown</svws-ui-button>
				</template>
				<template #dropdownItems>
					<svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
					<svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
					<svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
					<svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
				</template>
			</svws-ui-dropdown-with-action>
		</Variant>

		<Variant title="Disabled">
			<svws-ui-dropdown-with-action type="primary" disabled dropdown-disabled>
				<template #actionButton>
					<svws-ui-button dropdown-action @click="onClick">Button und Dropdown</svws-ui-button>
				</template>
				<template #dropdownItems>
					<svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
					<svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
					<svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
					<svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
				</template>
			</svws-ui-dropdown-with-action>
		</Variant>
	</Story>
</template>