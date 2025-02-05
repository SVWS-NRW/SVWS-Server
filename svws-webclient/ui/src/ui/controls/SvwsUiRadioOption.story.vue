<template>
	<Story title="Radio Options" id="svws-ui-radio-option" icon="ri:radio-button-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Horizontal Row" id="Horizontal Row">
			<div class="p-4">
				<svws-ui-radio-group :row="true">
					<svws-ui-radio-option value="option1" name="radioInputExample" label="Option 1" />
					<svws-ui-radio-option value="option2" name="radioInputExample" label="Option 2" disabled />
					<svws-ui-radio-option value="option3" name="radioInputExample" label="Option 3" />
					<svws-ui-radio-option value="option4" name="radioInputExample" label="Option 4">
						<span class="icon i-ri-forbid-line" />
					</svws-ui-radio-option>
					<svws-ui-radio-option value="option4" name="radioInputExample" label="Option 4" :icon="false" />
				</svws-ui-radio-group>
			</div>
		</Variant>

		<Variant title="Stacked" id="Stacked">
			<div class="p-4">
				<svws-ui-radio-group>
					<svws-ui-radio-option value="option1" name="radioInputExample2" label="Option 1" />
					<svws-ui-radio-option value="option2" name="radioInputExample2" label="Option 2" />
				</svws-ui-radio-group>
			</div>
		</Variant>

		<Variant title="Default" id="Default">
			<div class="flex p-4">
				<svws-ui-radio-option name="name" value="value" label="Option" />
			</div>
		</Variant>

		<Variant title="Checked" id="Checked">
			<div class="flex p-4">
				<svws-ui-radio-option v-model="checked" name="name" value="value" label="Option" />
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="flex p-4">
				<svws-ui-radio-option name="name" value="value" label="Option" statistics />
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="flex p-4">
				<svws-ui-radio-option name="name" value="value" label="Option" disabled />
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { ref } from "vue"
	const checked = ref('value');
</script>

<style lang="postcss">

	@reference "../../assets/styles/index.css";

</style>