
<template>
	<Story title="Spinner" id="svws-ui-spinner" icon="ri:loader-4-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Spinning">
			<svws-ui-spinner spinning />
		</Variant>
		<Variant title="Example">
			<svws-ui-button>
				Lädt
				<svws-ui-spinner spinning />
			</svws-ui-button>
		</Variant>
	</Story>
</template>
